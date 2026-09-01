package PracticalProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.* ;
import java.io.*;
import javax.swing.border.Border;

// 1. كلاس Task: يمثل تصميم "المهمة الواحدة" (مربع النص، الرقم، وزر Done , زر Delete)
class Task extends JPanel {
    AppFrame parent;   // مرجع للنافذة الرئيسية عشان نقدر نحذف المهمة
    JLabel index; // متغير لعرض رقم المهمة
    JTextField taskName; // متغير لمربع إدخال نص المهمة
    JButton done , delete; // متغير لزر الإنتهاء من المهمة

    // تعريف الألوان المستخدمة في التصميم
    Color pink = new Color(255, 161, 161); // لون خلفية المهمة (أحمر فاتح/وردي)
    Color doneColor = new Color(250, 70, 70); // لون زر الإنتهاء (أحمر غامق)
    Color DeleteColor = new Color(250 , 50,50) ; // لون زر الحذف (أحمر غامق جدا)
    Task(AppFrame parent ,String text) {
        this.parent = parent;   // ربط بالنافذة الرئيسية

        this.setPreferredSize(new Dimension(400, 30)); // تحديد حجم عرض وطول المهمة الواحدة
        this.setBackground(pink); // تعيين لون خلفية المهمة
        this.setLayout(new BorderLayout()); // استخدام تخطيط BorderLayout لتقسيم المهمة (يمين، وسط، يسار)

        index = new JLabel(""); // إنشاء مكان رقم المهمة (فارغ مبدئياً)
        index.setPreferredSize(new Dimension(100, 50)); // تحديد حجم مربع الرقم
        index.setHorizontalAlignment(JLabel.CENTER); // محاذاة الرقم ليكون في المنتصف
        this.add(index, BorderLayout.WEST); // إضافة الرقم في الجزء الأيسر من المهمة (West)

        taskName = new JTextField("Write something.."); // إنشاء مربع النص مع رسالة افتراضية
        taskName.setBorder(BorderFactory.createEmptyBorder()); // إزالة الحدود (البرواز) من مربع النص ليكون شكله مسطحاً
        taskName.setBackground(pink); // توحيد لون خلفية مربع النص مع لون المهمة
        taskName.setForeground(Color.GRAY);   // لون النص الافتراضي (رمادي)
        this.add(taskName, BorderLayout.CENTER); // إضافة مربع النص في منتصف المهمة (Center)

        if (text == null || text.isEmpty()) {
            taskName = new JTextField("Write something..");
            taskName.setForeground(Color.GRAY);
        } else {
            taskName = new JTextField(text);
            taskName.setForeground(Color.BLACK);
        }

        taskName.setBorder(BorderFactory.createEmptyBorder());
        taskName.setBackground(pink);
        this.add(taskName, BorderLayout.CENTER);

        // إضافة مستمع للتركيز (FocusListener) لمعرفة متى يدخل أو يخرج المستخدم من المربع
        taskName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {   // ده بيشتغل لما المستخدم يضغط داخل مربع النص
                if(taskName.getText().equals("Write something..")){  // لو النص الحالي هو النص الافتراضي
                    taskName.setText(""); //  امسح النص أول ما المستخدم يبدأ يكتب
                    taskName.setForeground(Color.BLACK); // خلي لون الكتابة أسود
                }
            }
            @Override
            public void focusLost(FocusEvent e) {  // ده بيشتغل لما المستخدم يخرج من مربع النص
                if (taskName.getText().isEmpty()) {  // لو المستخدم ساب المربع فاضي
                    taskName.setText("Write something.."); // يرجع النص الافتراضي تاني
                    taskName.setForeground(Color.GRAY); //يرجع لون النص الافتراضي (رمادي)
                }
            }
        });
        JPanel buttons = new JPanel(); // Panel عشان نحط فيه الزرارين
        buttons.setLayout(new GridLayout(1, 2)); //زرار جنب زرار
        buttons.setPreferredSize(new Dimension(130, 60)); // حجم ثابت ومتوازن

        done = new JButton("Done"); // إنشاء زر "Done"
        done.setBorder(BorderFactory.createEmptyBorder()); // إزالة حدود الزر
        done.setBackground(doneColor); // تعيين لون الزر
        done.setFocusPainted(false); // إزالة المربع الوهمي الذي يظهر حول النص عند الضغط على الزر
        done.addActionListener(e -> parent.rewriteFile());


        delete = new JButton("Delete"); // انشاء زر "Delete"
        delete.setBorder(BorderFactory.createEmptyBorder());
        delete.setBackground(DeleteColor); // لون مختلف
        delete.addActionListener(e -> parent.deleteTask(this)); // لما تضغط Delete يمسح المهمة

        buttons.add(done); // اضافة الزرارين جنب بعض
        buttons.add(delete);

        this.add(buttons, BorderLayout.EAST); // إضافة الـ panel في اليمين
    }
}

// 2. كلاس List: يمثل المكان (القائمة) الذي ستتجمع فيه جميع المهام
class List extends JPanel {
    Color lightColor = new Color(252, 221, 176); // تعريف لون الخلفية (أصفر/برتقالي فاتح)

    List() {
        GridLayout layout = new GridLayout(20, 1); // استخدام تخطيط شبكي يسمح بـ 20 صفوف وعمود واحد
        layout.setVgap(5); // إضافة مسافة عمودية (فراغ) بين كل مهمة والتي تليها بمقدار 5 بيكسل
        this.setLayout(layout); // تطبيق التخطيط على القائمة
        this.setPreferredSize(new Dimension(400, 560)); // تحديد أبعاد القائمة (العرض والطول)
        this.setBackground(lightColor); // تعيين لون خلفية القائمة
    }
}

// 3. كلاس Footer: يمثل الجزء السفلي الذي يحتوي على أزرار التحكم
class Footer extends JPanel {
    JButton addTask; // متغير لزر إضافة مهمة
    JButton clear; // متغير لزر مسح المهام

    Color orange = new Color(233, 133, 128); // لون الأزرار
    Color lightColor = new Color(252, 221, 176); // لون خلفية الفوتر
    Border emptyBorder = BorderFactory.createEmptyBorder(); // متغير لإزالة الحدود

    AppFrame parent ;   // مرجع للنافذة
    Footer(AppFrame parent) {
        this.parent = parent ;
        this.setPreferredSize(new Dimension(400, 60)); // تحديد حجم الفوتر
        this.setBackground(lightColor); // تعيين لون الخلفية

        addTask = new JButton("Add Task"); // إنشاء زر الإضافة
        addTask.setBorder(emptyBorder); // إزالة حدود الزر
        addTask.setFont(new Font("Sans-serif", Font.ITALIC, 20)); // تغيير نوع، وميلان، وحجم الخط
        addTask.setVerticalAlignment(JButton.BOTTOM); // محاذاة النص لأسفل الزر
        addTask.setBackground(orange); // تعيين لون الزر

        addTask.addActionListener(e -> parent.addTask());   // اضافة مهمة
        this.add(addTask); // إدراج الزر داخل لوحة الفوتر
        this.add(Box.createHorizontalStrut(20)); // إضافة مسافة فارغة (فاصل شفاف) بين الزر الأول والثاني بمقدار 20 بيكسل

        clear = new JButton("Clear All"); // إنشاء زر مسح المهام المكتملة
        clear.setFont(new Font("Sans-serif", Font.ITALIC, 20)); // تغيير خط الزر
        clear.setBorder(emptyBorder); // إزالة حدود الزر
        clear.setBackground(orange); // تعيين لون الزر

        clear.addActionListener(e -> parent.clearTasks());  //مسح كل المهام
        this.add(clear); // إدراج الزر داخل الفوتر
    }

}

// 4. كلاس TitleBar: يمثل الشريط العلوي لعنوان البرنامج
class TitleBar extends JPanel {
    Color lightColor = new Color(252, 221, 176); // لون الخلفية

    TitleBar() {
        this.setPreferredSize(new Dimension(400, 80)); // تحديد أبعاد شريط العنوان
        this.setBackground(lightColor); // تعيين لون الخلفية

        JLabel titleText = new JLabel("To Do List"); // إنشاء نص العنوان
        titleText.setPreferredSize(new Dimension(200, 60)); // تحديد الحجم المخصص للنص
        titleText.setFont(new Font("Sans-serif", Font.BOLD, 20)); // جعل الخط عريض (Bold) وبحجم 20
        titleText.setHorizontalAlignment(JLabel.CENTER); // محاذاة العنوان ليكون في منتصف الشاشة

        this.add(titleText); // إضافة النص داخل شريط العنوان
    }
}

// 5. كلاس AppFrame: النافذة الرئيسية التي تجمع كل الأجزاء السابقة معاً (الهيكل الأساسي)
class AppFrame extends JFrame {
    private TitleBar title; // تجهيز مكان لشريط العنوان
    private Footer footer; // تجهيز مكان للفوتر
    private List list; // تجهيز مكان للقائمة

    private int  taskCount = 1 ;

    AppFrame() {
        this.setSize(400, 600); // تحديد حجم النافذة الكلية للبرنامج (العرض 400، الطول 600)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // إغلاق البرنامج نهائياً وإيقافه من الخلفية عند الضغط على علامة X
        this.setLayout(new BorderLayout());   // تخطيط الشكل

        title = new TitleBar(); // استدعاء تصميم شريط العنوان
        footer = new Footer(this); // استدعاء تصميم الفوتر
        list = new List(); // استدعاء تصميم القائمة

        this.add(title, BorderLayout.NORTH); // وضع شريط العنوان في الجزء العلوي من النافذة (North)
        this.add(footer, BorderLayout.SOUTH); // وضع الفوتر في الجزء السفلي من النافذة (South)
        this.add(list, BorderLayout.CENTER); // وضع القائمة في المنتصف لتأخذ باقي المساحة المتاحة (Center)

        loadTasksFromFile(); //  قراءة المهام عند التشغيل
        this.setVisible(true); // إظهار النافذة للمستخدم على الشاشة

    }
    // اضافة مهمة
    public void addTask(){
            Task task = new Task(this, "");  // نص افتراضي وانشاء مهمة

            // إضافة مؤقتة لجلب النص من المستخدم
            String text = JOptionPane.showInputDialog(this, "اكتب اسم المهمة:", "مهمة جديدة", JOptionPane.PLAIN_MESSAGE);

            // لو المستخدم ضغط Cancel أو سبها فاضية
            if (text == null || text.trim().isEmpty()) return;

            task.taskName.setText(text);
            task.taskName.setForeground(Color.BLACK);
           task.index.setText(String.valueOf(taskCount)); // رقم المهمة
            taskCount++; // زيادة العداد في حالة اضافة مهمة

        list.add(task) ; // اضافة المهمة للقائمة
        list.revalidate(); // تحديث
        list.repaint(); // اعادة رسم
        saveTaskToFile(text) ;   // حفظ في الملف

    }
    //   حذف مهمة
    public void deleteTask(Task task){
        list.remove(task);   // حذف من الشاشة
        list.revalidate(); // تحديث
        list.repaint(); // اعادة رسم

        rewriteFile() ; //   اعادة كتابة الملف بعد الحذف
    }
    // حذف كل المهام
    public void clearTasks(){
        list.removeAll();   // حذف كل العناصر
        list.revalidate();  // تحديث
        list.repaint();   // إعادة رسم
        taskCount = 1 ; // إعادة العداد

        try {
            new FileWriter("Output.txt").close(); // يمسح الملف (تفريغ)
        } catch (IOException e) {
            e.printStackTrace();  // طباعة الخطأ (ان وجد الخطأ)
        }
    }
      // حفظ مهمة (تم تنفيذها)
    private  void saveTaskToFile(String text){
        try{
            BufferedWriter bwriter = new BufferedWriter(new FileWriter("Output.txt",true)) ; // فتح الملف للإضافة
            bwriter.write(text); // كتابة النص
            bwriter.newLine();   // سطر جديد
            bwriter.close();   // غلق الملف
        } catch (IOException e) {
            e.printStackTrace(); // طباعة الخطأ
        }
    }
    //   قراءة المهام
    private void loadTasksFromFile(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Output.txt")) ; // فتح الملف
            String line ;    // تعريف نص

            while ((line = reader.readLine()) != null){   // قراءة كل سطر
                Task task = new Task(this , line) ; // انشاء مهمة بالنص الجديد
                task.index.setText(String.valueOf(taskCount));  // رقم المهمة بالنص
                taskCount++;
                list.add(task) ; // اضافة المهمة بالقائمة
            }
            reader.close();  // غلق الملف
        } catch (IOException e){
            // أول مرة الملف مش موجود عادي
        }
    }
    //  إعادة كتابة الملف بعد الحذف
    public void rewriteFile(){
        try{
           BufferedWriter writer = new BufferedWriter(new FileWriter("Output.txt")) ; // فتح الملف (مسح القديم)
            Component[] tasks = list.getComponents() ;  // جلب كل المهام

            for(Component c : tasks){
                Task t =(Task) c ;  // تحويل العنصر إلى Task
                writer.write(t.taskName.getText());  // كتابة النص
                  writer.newLine(); // سطر جديد
            }
            writer.close();   // غلق الملف
        } catch (IOException e) {
           e.printStackTrace();  // طباعة الخطأ
        }
    }
}

